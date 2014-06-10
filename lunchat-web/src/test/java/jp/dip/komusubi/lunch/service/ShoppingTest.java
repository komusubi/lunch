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
package jp.dip.komusubi.lunch.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.lunchat.core.model.Group;
import jp.lunchat.core.model.Order;
import jp.lunchat.core.model.OrderLine;
import jp.lunchat.core.model.Product;
import jp.lunchat.core.model.Shop;
import jp.lunchat.core.model.User;
import jp.lunchat.storage.Basket;
import jp.lunchat.storage.dao.OrderDao;
import jp.lunchat.storage.dao.OrderLineDao;
import jp.lunchat.storage.dao.ProductDao;
import jp.lunchat.storage.dao.ShopDao;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.komusubi.common.util.Resolver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author jun.ozeki
 * @since 2012/04/08
 */
public class ShoppingTest /* extends Shopping */ {

    @Mock private OrderDao orderDao;
    @Mock private OrderLineDao orderLineDao;
    @Mock private ProductDao productDao;
    @Mock private ShopDao shopDao;
    @Mock private Resolver<Date> dateResolver;
    
    private Shopping target;
    private User user;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }
    
    private void scenario() {
        target = new Shopping(user, new Basket(), orderDao, orderLineDao, productDao, shopDao, dateResolver);
    }
    
    @Test
    public void グループ注文() throws Exception {
        Integer groupId = 10;
        Group group = new Group(groupId);
        String shop1Id = "shop1";
        String shop2Id = "shop2";
        Shop shop1 = new Shop(shop1Id)
                        .setName("店舗1");
        Shop shop2 = new Shop(shop2Id)
                        .setName("店舗2");
        user = new User(50);
        Product p1 = new Product("1")
                        .setAmount(100)
                        .setName("商品1")
                        .setShop(shop1);

        Product p2 = new Product("2")
                        .setAmount(200)
                        .setName("商品2")
                        .setShop(shop1);
        
        Product p3 = new Product("3")
                        .setAmount(300)
                        .setName("商品3")
                        .setShop(shop2);
        
        Date orderDate = DateUtils.parseDate("2012/04/08 23:00:00", new String[]{"yyyy/MM/dd HH:mm:ss"});
        
        OrderLine orderLine1 = new OrderLine()
                                .setProduct(p1)
                                .setQuantity(2)
                                .setDatetime(orderDate);
        OrderLine orderLine2 = new OrderLine()
                                .setProduct(p2)
                                .setQuantity(1)
                                .setDatetime(orderDate);
        
        List<Order> orders1 = new ArrayList<>();
        List<Order> orders2 = new ArrayList<>();
        Order order1 = new Order()
                        .setGroup(group)
                        .setSummary(false)
                        .setCancel(false)
                        .setDatetime(orderDate)
                        .setUser(user);
        order1.addLine(orderLine1);
        orders1.add(order1);
        
        when(dateResolver.resolve()).thenReturn(orderDate);
        when(orderDao.findByGroupIdAndDate(groupId, orderDate, false)).thenReturn(orders1);
        when(orderDao.findByGroupIdAndDate(groupId, orderDate, true)).thenReturn(orders2);
        when(orderDao.persist(order1)).thenReturn(1);
        
        scenario();
        target.order(group);
        
        verify(orderDao).findByGroupIdAndDate(groupId, orderDate, false);
        verify(orderDao).findByGroupIdAndDate(groupId, orderDate, true);
        order1.setSummary(true);
        verify(orderDao, times(1)).persist(order1);
    }
    
    private Group getGroup() {
        return new Group()
                    .setCode("CDE")
                    .setId(1)
                    .setName("グループ名")
                    .setPhoneNumber("03-0000-0000");
    }
    
    private Shop getShop() {
        return new Shop()
                    .setLastOrder(new Date())
                    .setName("お弁当屋")
                    .setPhoneNumber("03-1234-9999")
                    .setUrl("http://localhost");
    }
    
    private User getUser() {
        return new User() 
                    .setEmail("name@email.com")
                    .setGroup(getGroup())
                    .setJoined(new Date())
                    .setName("山田 太郎")
                    .setNickname("あだ名")
                    .setPassword("password");
    }
    
    private Product getProduct() {
        return new Product()
                    .setAmount(400)
                    .setFinish(new Date())
                    .setName("幕の内")
                    .setRefId("")
                    .setShop(getShop())
                    .setStart(new Date());
    }
    
    /**
     * cancel order line.
     * @throws Exception
     */
    @Test
    public void cancelOrderLine() throws Exception {
        Order order = new Order()
                            .setAmount(360)
                            .setCancel(false)
                            .setDatetime(new Date())
                            .setGroup(getGroup())
                            .setShop(getShop())
                            .setUser(getUser());
        OrderLine orderLine = new OrderLine()
                                    .setAmount(400)
                                    .setCancel(false)
                                    .setDatetime(new Date())
                                    .setProduct(getProduct())
                                    .setProduct(getProduct())
                                    .setQuantity(1);

        when(orderDao.find(orderLine.getPrimaryKey().getOrderId())).thenReturn(order);
        order.setCancel(true);
        doNothing().when(orderDao).update(order);
        scenario();
        target.cancel(orderLine);
        verify(orderDao, times(1)).find(orderLine.getPrimaryKey().getOrderId());
        verify(orderDao, times(1)).update(order);
    }
}
