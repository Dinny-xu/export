package com.example.web.core.Handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.graphbuilder.math.func.LgFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: test_pdf
 * @description: mybatis-plus 处理器
 * @author: Xu·yan
 * @create: 2020-12-22 11:40
 **/
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    /**
     * @description 插入策略
     * @author Xu·yan
     * @date 2020/12/22 11:42 上午
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill.....");
        this.setFieldValByName("create_time", new Date(), metaObject);
        this.setFieldValByName("update_time", new Date(), metaObject);
    }

    /**
     * @description 更新策略
     * @author Xu·yan
     * @date 2020/12/22 11:43 上午
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill.....");
        this.setFieldValByName("update_time", new Date(), metaObject);
    }
}
