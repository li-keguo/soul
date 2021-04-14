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

package org.dromara.soul.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.soul.admin.model.entity.UserRoleDO;

import java.util.List;

/**
 * this is User Role Mapper.
 *
 * @author nuo-promise
 **/
@Mapper
public interface UserRoleMapper {

    /**
     * select role by id.
     *
     * @param id primary key.
     * @return {@linkplain UserRoleDO}
     */
    UserRoleDO selectById(String id);

    /**
     * find by user id.
     *
     * @param userId user id
     * @return {@linkplain List}
     */
    List<UserRoleDO> findByUserId(String userId);

    /**
     * insert user role.
     *
     * @param userRoleDO {@linkplain UserRoleDO}
     * @return rows int
     */
    int insert(UserRoleDO userRoleDO);

    /**
     * insert selective user role.
     *
     * @param userRoleDO {@linkplain UserRoleDO}
     * @return rows int
     */
    int insertSelective(UserRoleDO userRoleDO);

    /**
     * delete user role.
     *
     * @param id primary key
     * @return rows int
     */
    int delete(String id);

    /**
     * delete role by user id.
     *
     * @param userId user id
     * @return row int
     */
    int deleteByUserId(String userId);
}
