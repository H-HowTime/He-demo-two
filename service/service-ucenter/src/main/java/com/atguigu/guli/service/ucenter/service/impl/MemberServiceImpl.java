package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.common.util.MD5;
import com.atguigu.guli.service.base.consts.ServiceConstants;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-30
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(RegisterForm registerForm) {
        if (registerForm == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        //1、获取表单数据
        String nickname = registerForm.getNickname();
        String mobile = registerForm.getMobile();
        String password = registerForm.getPassword();
        String code = registerForm.getCode();
        //2、校验表单数据

        if (StringUtils.isEmpty(nickname)
                || StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || FormUtils.isMobile(code)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        //3、校验验证码
        String redisCodeKey = ServiceConstants.SMS_CODE_PREFIX + mobile;  //redis中储存验证码的key
        Object checkCode = redisTemplate.opsForValue().get(redisCodeKey);
        if (checkCode == null) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }
        if (!code.equals(checkCode.toString())) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }
        //4、验证手机号是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }
        //5、完成用户注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");//设置默认头像
        //使用MD5给密码加密 TODO 研究一下加密方式
        String MD5Password = MD5.encrypt(password);
        member.setPassword(MD5Password);
        baseMapper.insert(member);
        //删除验证码
        //redisTemplate.delete(redisCodeKey);
    }

    @Override
    public String login(LoginForm loginForm) {
        //获取手机号和密码
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();
        //先判断账户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        //判断密码是否正确
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //生成jwt
        JwtInfo jwtInfo = new JwtInfo(member.getId(), member.getNickname(), member.getAvatar());
        String token = JwtHelper.createToken(jwtInfo);
        return token;
    }

    @Override
    public MemberDto getMemberInfo(String memberId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", memberId);
        queryWrapper.select("id", "mobile", "nickname");
        Member member = baseMapper.selectOne(queryWrapper);
        MemberDto memberDto = new MemberDto();
//        System.out.println(member);
        BeanUtils.copyProperties(member, memberDto);
//        System.out.println(memberDto);
        return memberDto;
    }

    @Override
    public int getMemberNum(String dateDay) {
        return baseMapper.getMemberNum(dateDay);
    }
}
