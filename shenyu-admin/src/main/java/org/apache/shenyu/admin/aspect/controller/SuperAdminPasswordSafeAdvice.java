/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.admin.aspect.controller;

import com.google.common.base.Stopwatch;
import org.apache.shenyu.admin.config.properties.DashboardProperties;
import org.apache.shenyu.admin.mapper.DashboardUserMapper;
import org.apache.shenyu.admin.model.entity.DashboardUserDO;
import org.apache.shenyu.admin.utils.SessionUtil;
import org.apache.shenyu.common.exception.ShenyuException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * <h3>SuperAdminPasswordSafeAdvice.</h3>
 * <p>The Super Administrator account has some privileges that are dangerous and unique to the account</p>
 * <p>This is implemented to ensure that the super administrator account password is secure</p>
 * <p>1 The password is the initial password, which we consider dangerous and will force it to change the password if the user is so set</p>
 * <p>2 If the password has not been changed for a long time, we consider this risky and will force the user to change the password</p>
 */
@Component
public class SuperAdminPasswordSafeAdvice implements ControllerMethodAdvice {
    
    private final DashboardProperties properties;
    
    private final DashboardUserMapper userMapper;
    
    public SuperAdminPasswordSafeAdvice(final DashboardProperties properties,
                                        final DashboardUserMapper userMapper) {
        this.properties = properties;
        this.userMapper = userMapper;
    }
    
    @Override
    public void doPreProcess(final Object bean, final Method method, final Stopwatch stopwatch) {
        // If the Super Administrator privilege attribute is not enabled
        if (!Boolean.TRUE.equals(properties.getEnableOnlySuperAdminPermission())) {
            // skip
            return;
        }
        if (!Boolean.TRUE.equals(properties.getEnableSuperAdminPasswordSafe())) {
            return;
        }
        // if not supper admin
        if (!SessionUtil.isAdmin()) {
            // skip
            return;
        }
        
        final RequiresPermissions permissions = AnnotatedElementUtils.findMergedAnnotation(method, RequiresPermissions.class);
        if (Objects.isNull(permissions) || Objects.isNull(permissions.value())) {
            return;
        }
        // This method exists in the list of super administrator privileges
        if (properties.getOnlySuperAdminPermission()
                .stream()
                .anyMatch(p -> Arrays.asList(permissions.value()).contains(p))) {
            
            final String userId = SessionUtil.visitor().getUserId();
            final DashboardUserDO userDO = userMapper.selectById(userId);
            if (Objects.equals(userDO.getDateCreated(), userDO.getDateUpdated())) {
                throw new ShenyuException("The password is the default password and you must complete the change once");
            }
            // The password has not been changed for a long time
            if (userDO.getDateUpdated().getTime() <= System.currentTimeMillis() - properties.getSuperAdminPasswordValidDuration()) {
                throw new ShenyuException("If the password has not been changed for a long time, please use it after changing it to ensure the security of the super administrator account");
            }
            // Weak password blacklist
        }
        
    }
    
    
}
