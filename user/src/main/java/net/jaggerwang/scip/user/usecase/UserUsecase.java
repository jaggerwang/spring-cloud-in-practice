package net.jaggerwang.scip.user.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.util.encoder.PasswordEncoder;
import net.jaggerwang.scip.common.util.generator.RandomGenerator;
import net.jaggerwang.scip.common.entity.RoleEntity;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.common.entity.UserEntity;
import net.jaggerwang.scip.user.usecase.port.dao.RoleDAO;
import net.jaggerwang.scip.user.usecase.port.dao.UserDAO;

public class UserUsecase {
    private static HashMap<String, String> mobileVerifyCodes = new HashMap<>();
    private static HashMap<String, String> emailVerifyCodes = new HashMap<>();

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private RandomGenerator randomGenerator = new RandomGenerator();
    private PasswordEncoder passwordEncoder = new PasswordEncoder();

    public UserUsecase(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    public UserEntity register(UserEntity userEntity) {
        if (userDAO.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new UsecaseException("用户名重复");
        }

        var user = UserEntity.builder().username(userEntity.getUsername())
                .password(passwordEncoder.encode(userEntity.getPassword())).build();
        return userDAO.save(user);
    }

    public Boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public UserEntity modify(Long id, UserEntity userEntity) {
        var user = userDAO.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("用户未找到");
        }

        if (userEntity.getUsername() != null) {
            if (userDAO.findByUsername(userEntity.getUsername()).isPresent()) {
                throw new UsecaseException("用户名重复");
            }
            user.setUsername(userEntity.getUsername());
        }
        if (userEntity.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        if (userEntity.getMobile() != null) {
            if (userDAO.findByMobile(userEntity.getMobile()).isPresent()) {
                throw new UsecaseException("手机重复");
            }
            user.setMobile(userEntity.getMobile());
        }
        if (userEntity.getEmail() != null) {
            if (userDAO.findByEmail(userEntity.getEmail()).isPresent()) {
                throw new UsecaseException("邮箱重复");
            }
            user.setEmail(userEntity.getEmail());
        }
        if (userEntity.getAvatarId() != null) {
            user.setAvatarId(userEntity.getAvatarId());
        }
        if (userEntity.getIntro() != null) {
            user.setIntro(userEntity.getIntro());
        }

        return userDAO.save(user);
    }

    public String sendMobileVerifyCode(String type, String mobile) {
        var key = String.format("%s_%s", type, mobile);
        if (mobileVerifyCodes.get(key) == null) {
            var code = randomGenerator.numberString(6);
            mobileVerifyCodes.put(key, code);
        }
        return mobileVerifyCodes.get(key);
    }

    public Boolean checkMobileVerifyCode(String type, String mobile, String code) {
        var key = String.format("%s_%s", type, mobile);
        if (code != null && code.equals(mobileVerifyCodes.get(key))) {
            mobileVerifyCodes.remove(key);
            return true;
        } else {
            return false;
        }
    }

    public String sendEmailVerifyCode(String type, String email) {
        var key = String.format("%s_%s", type, email);
        if (emailVerifyCodes.get(key) == null) {
            var code = randomGenerator.numberString(6);
            emailVerifyCodes.put(key, code);
        }
        return emailVerifyCodes.get(key);
    }

    public Boolean checkEmailVerifyCode(String type, String email, String code) {
        var key = String.format("%s_%s", type, email);
        if (code != null && code.equals(emailVerifyCodes.get(key))) {
            emailVerifyCodes.remove(key);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserEntity> info(Long id) {
        return userDAO.findById(id);
    }

    public Optional<UserEntity> infoByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public Optional<UserEntity> infoByMobile(String mobile) {
        return userDAO.findByMobile(mobile);
    }

    public Optional<UserEntity> infoByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public List<RoleEntity> roles(String username) {
        return roleDAO.rolesOfUser(username);
    }

    public void follow(Long followerId, Long followingId) {
        userDAO.follow(followerId, followingId);
    }

    public void unfollow(Long followerId, Long followingId) {
        userDAO.unfollow(followerId, followingId);
    }

    public Boolean isFollowing(Long followerId, Long followingId) {
        return userDAO.isFollowing(followerId, followingId);
    }

    public List<UserEntity> following(Long followerId, Long limit, Long offset) {
        return userDAO.following(followerId, limit, offset);
    }

    public Long followingCount(Long followerId) {
        return userDAO.followingCount(followerId);
    }

    public List<UserEntity> follower(Long followingId, Long limit, Long offset) {
        return userDAO.follower(followingId, limit, offset);
    }

    public Long followerCount(Long followingId) {
        return userDAO.followerCount(followingId);
    }
}
