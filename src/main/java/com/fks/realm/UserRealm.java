package com.fks.realm;

import com.fks.domain.Role;
import com.fks.domain.User;
import com.fks.service.IResService;
import com.fks.service.IRoleService;
import com.fks.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IResService resService;

    private String salt;

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("==================================");
        System.out.println("授权操作");
        System.out.println("==================================");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        String username = (String) principals.getPrimaryPrincipal();
        User user = userService.findUserByName(username);

        if(user.getRole_id() == null) {
            info.addRole("guest");
        } else {
            System.out.println(user.getRole_id());
            Role role = roleService.findRoleById(user.getRole_id());
            info.addRole(role.getRole_name());

            List<String> reses = resService.findAllResNameByRoleId(role.getId());

            info.addStringPermissions(reses);
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("==================================");
        System.out.println("登录操作");
        System.out.println("==================================");

        String username = (String) token.getPrincipal();
        User user = userService.findUserByName(username);
        if(user == null) {
            throw new UnknownAccountException("账号不存在");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, user.getPassword(), ByteSource.Util.bytes(salt), this.getName());

        return info;
    }
}
