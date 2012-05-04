/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package jp.dip.komusubi.lunch.module.dao;

import java.util.Date;
import java.util.List;

import jp.dip.komusubi.lunch.model.Order;

import org.komusubi.common.persistence.GenericDao;

/**
 * order dao.
 * @author jun.ozeki
 * @since 2011/11/26
 */
public interface OrderDao extends GenericDao<Integer, Order> {

	List<Order> findByUserAndDate(Integer userId, Date date);
	List<Order> findByUser(Integer userId);
	List<Order> findByProduct(String productId);
	List<Order> findByUserAndProductAndDate(Integer userId, String productId, Date date);
	List<Order> findByGroupId(String groupId);
	List<Order> findByGroupIdAndDate(Integer groupId, Date date, boolean summary);

}
