package com.gaoxiaocha;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoxiaocha.mapper.ClassMapper;
import com.gaoxiaocha.mapper.DynamicsMapper;
import com.gaoxiaocha.mapper.UserMapper;
import com.gaoxiaocha.pojo.Classes;
import com.gaoxiaocha.pojo.Dynamics;
import com.gaoxiaocha.pojo.ImToken;
import com.gaoxiaocha.pojo.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GaoxiaochaApplicationTests {

    @Resource
    private ClassMapper classMapper;

    @Test
    public void selectByStuNo() {
        List<Classes> classes = classMapper.selectList(new QueryWrapper<Classes>().eq("stu_no",2017210630));
        for (Classes aClass : classes) {
            System.out.println(aClass);
        }
    }

    @Resource
    private UserMapper userMapper;

    @Test
    public void signUp() {
        User user = new User();
        user.setUserAccount("123456");
        user.setUserPwd("123456789");
        user.setUserName("555");
        User one = userMapper.selectOne(new QueryWrapper<User>().eq("user_account", user.getUserAccount()));
        if (one == null) {
            int i = userMapper.insert(user);
            if (i == 1) {
                System.out.println(true);
            }
        }
        System.out.println(false);
    }

    @Resource
    private DynamicsMapper dynamicsMapper;

    @Test
    public void getDynamics() {
        QueryWrapper<Dynamics> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        Page<Dynamics> page = new Page<>(3,5);
        Page<Dynamics> dynamicsPage = dynamicsMapper.selectPage(page, queryWrapper);
        System.out.println(dynamicsPage.getSize());
        System.out.println(JSON.toJSONString(dynamicsPage));
        List<Dynamics> records = page.getRecords();
    }

    @Test
    public void getDynamicsById() {
        QueryWrapper<Dynamics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",8);
        queryWrapper.orderByDesc("gmt_create");
        List<Dynamics> dynamics = dynamicsMapper.selectList(queryWrapper);
        System.out.println(JSON.toJSONString(dynamics));
    }

    @Test
    public void publish(){
        Dynamics dynamics = new Dynamics();
        dynamics.setUserId(6);
        dynamics.setUserName("zyh");
        dynamics.setContent("hhhh");
        dynamics.setImg(null);
        dynamics.setGmtCreate(System.currentTimeMillis());
        dynamics.setCommentCount(0);
        dynamics.setLikeCount(0);
        int insert = dynamicsMapper.insert(dynamics);
        Integer id = dynamics.getId();
        System.out.println(id);
    }

    @Test
    public void register() {
        OkHttpClient client = new OkHttpClient();
        String appSecret = "HNQljAwFNI";
        String url = "https://api-cn.ronghub.com/user/getToken.json";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomNum = String.valueOf((Math.random() * 9 + 1) * Math.pow(10, 17)) ;
        String signature = DigestUtils.sha1Hex(appSecret + randomNum + timestamp).toString();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("userId","h254234211");
        builder.add("name","zyh");
        builder.add("portraitUri","null");
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).header("App-Key","cpj2xarlchkkn")
                .header("Nonce",randomNum)
                .header("Timestamp",timestamp)
                .header("Signature",signature).post(body).build();
        try (Response response = client.newCall(request).execute()){
            String responseBody = response.body().string();
            Map maps = JSON.parseObject(responseBody);
            String token = String.valueOf(maps.get("token")) ;
            String userId = String.valueOf(maps.get("userId"));
            ImToken imToken = new ImToken();
            imToken.setToken(token);
            imToken.setUserAccount(userId);
//            tokenMapper.insert(imToken);
            System.out.println(imToken.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
