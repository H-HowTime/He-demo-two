package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.service.base.consts.ServiceConstants;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.HttpClientUtils;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.ApiWxService;
import com.atguigu.guli.service.ucenter.utils.WxProperties;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

/**
 * @author hehao
 * @create 2021-01-02 16:36
 */
@Slf4j
@Service
public class ApiWxServiceImpl implements ApiWxService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private WxProperties wxProperties;

    private String appId;
    private String appSecret;
    private String redirectUri;

    @PostConstruct //初始化wxProperties属性
    public void init() {
        appId = wxProperties.getAppId();
        appSecret = wxProperties.getAppSecret();
        redirectUri = wxProperties.getRedirectUri();
    }

    @Override
    public String genQrConnect(HttpSession session) {
        String qrCodeUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +  //%s字符串中的占位符
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect ";
        //处理回调的url（重定向地址，需要进行UrlEncode）
        String redirectUriEn;
        try {
            redirectUriEn = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
        //处理state，随机生成字符串，保存到session域中
        String state = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute(ServiceConstants.WX_STATE_KEY, state);

        qrCodeUrl = String.format(qrCodeUrl, appId, redirectUriEn, state);

        return qrCodeUrl;
    }

    @Override
    public String callback(String code, String state, HttpSession session) {
        //获取session中的state
        Object attribute = session.getAttribute(ServiceConstants.WX_STATE_KEY);
        //判断参数是否合法
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state) || !state.equals(attribute)) {
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        getAccessTokenUrl = String.format(getAccessTokenUrl, appId, appSecret, code);
        //通过httpclient获取access-token
        HttpClientUtils client = new HttpClientUtils(getAccessTokenUrl);
        String content;
        try {
            client.get();
            content = client.getContent();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        /*
         *将content（json字符串）转换为map
         * access_token=40_IpzGGWTMXwvUhoRH4Egin1mNUelFWLHz_UpirmcLbA0Vu2h5o_i8b1JG3Yi1V9Bb9E47920Vs6HyFCYeO9xzs8xJou4VqGu1IGE9pC590Jo,
         * expires_in=7200.0,
         * refresh_token=40_FaTGe0lG5lWJ6jgkNhPmfFo2k3VmkAhCWRLxTUW1_DJRMysA9t_Me_KfcK1V8-s3-kK9b3GMPckBctTIii-6nRzy5IMoe0O4UA489e69iZ8,
         * openid=o3_SC5-CV6fB98_IChoNHSSyxENw,
         * scope=snsapi_login,
         * unionid=oWgGz1MeUo8knIUwaGhHUYdlvWcc
         */
        Gson gson = new Gson();
        Map contentMap = gson.fromJson(content, Map.class);
        //判断微信的响应状态
        if(contentMap.get("errcode") != null){
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        System.out.println(contentMap);
        //获取access-token和openid
        Object accessTokenObj = contentMap.get("access_token");
        if(accessTokenObj == null || StringUtils.isEmpty(accessTokenObj)){
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        String accessToken = accessTokenObj.toString();
        String openid = (String) contentMap.get("openid");
        //根据openid去数据库获取用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid",openid);
        Member member1 = memberMapper.selectOne(queryWrapper);
        if(member1 != null){ //TODO wx用户缓存问题，判断创建或者更新时间，如果是七天前就重新获取微信信息，并更新数据库
            //生成jwt
            JwtInfo jwtInfo = new JwtInfo(member1.getId(),member1.getNickname(),member1.getAvatar());
            String token = JwtHelper.createToken(jwtInfo);
            System.out.println("根据openid查询数据库用户信息");
            return token;
        }
        System.out.println("根据accessToken和openid调用接口获取用户信息");
        /*
            根据accessToken和openid调用接口获取用户信息
            http请求方式: GET
            https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
         */
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        getUserInfoUrl = String.format(getUserInfoUrl, accessToken, openid);
        HttpClientUtils client1 = new HttpClientUtils(getUserInfoUrl);
        String content1;
        try {
            client1.get();
            content1 = client1.getContent();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        /*
         *将content1（json字符串）转换为map
         * openid=o3_SC5-CV6fB98_IChoNHSSyxENw,
         * nickname=何、小浩,
         * sex=1.0,
         * language=zh_CN,
         * city=Zhengzhou,
         * province=Henan,
         * country=CN,
         * headimgurl=https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKF7FeGcRm3X7kDBwQ8wxbSbmETJicLp8fe8BwRQia24iajY6YdCRgpMFCwLEDRVNY9a2gQdkBKaU1FA/132,
         * privilege=[],
         * unionid=oWgGz1MeUo8knIUwaGhHUYdlvWcc
         */
        Map UserInfoMap = gson.fromJson(content1, Map.class);
        System.out.println(UserInfoMap);
        //判断微信的响应状态
        if(UserInfoMap.get("errcode") != null){
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        //解析用户信息
        String nickname = (String) UserInfoMap.get("nickname");
        String headimgurl = (String) UserInfoMap.get("headimgurl");
        double sex = (double) UserInfoMap.get("sex");
        //将用户信息保存到数据库 openid和微信用户是一对一的关系
        Member member = new Member();
        member.setNickname(nickname);
        member.setOpenid(openid);
        member.setAvatar(headimgurl);
        member.setSex((int)sex);
        memberMapper.insert(member);
        //生成jwt
        JwtInfo jwtInfo = new JwtInfo(member.getId(),member.getNickname(),member.getAvatar());
        String token = JwtHelper.createToken(jwtInfo);
        return token;
    }
}
