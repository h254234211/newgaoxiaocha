package com.gaoxiaocha.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoxiaocha.mapper.StuMapper;
import com.gaoxiaocha.pojo.Classes;
import com.gaoxiaocha.pojo.Friend;
import com.gaoxiaocha.pojo.Stu;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * StuService
 *
 * @author zyh
 * @date 2020/5/27
 */
@Service
public class StuService {

    @Resource
    private StuMapper stuMapper;

    public Stu stuInfo(Integer stuNo) {
        Stu stu = null;
        OkHttpClient client = new OkHttpClient();
        String url = "http://jwc.cqupt.edu.cn/data/json_StudentSearch.php?searchKey=" + stuNo;
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String stuInfo = response.body().string();
            JSONObject jsonObject = JSON.parseObject(stuInfo);
            JSONArray returnDatas = jsonObject.getJSONArray("returnData");
            JSONArray resultArray = new JSONArray();
            for (int i = 0; i < returnDatas.size(); i++) {
                JSONObject json = returnDatas.getJSONObject(i);
                stu = new Stu();
                stu.setZym(json.getString("zym"));
                stu.setYxm(json.getString("yxm"));
                stu.setXm(json.getString("xm"));
                stu.setBj(json.getString("bj"));
                stu.setStuNo(json.getString("xh"));
                resultArray.add(stu);
                stuMapper.insert(stu);
            }
            return stu;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stu getInfo(Integer stuNo) {
        Stu stu = stuMapper.selectOne(new QueryWrapper<Stu>().eq("stu_no", stuNo));
        return stu;
    }

    public int insert(Stu stu){
        return stuMapper.insert(stu);
    }
    public List<Stu> select(){
        return stuMapper.selectList(null);
    }
    public int update(Stu stu){
        return stuMapper.updateById(stu);
    }
    public int delete(Stu stu){
        return stuMapper.deleteById(stu.getId());
    }

    public List<Stu> queryForPage(int currentPage, int numPerPage){
        IPage<Stu> stuIPage = new Page<>(currentPage,numPerPage);
        stuIPage = stuMapper.selectPage(stuIPage,null);
        List<Stu> list = stuIPage.getRecords();
        return  list;
    }

    public int count(){
        QueryWrapper<Stu> queryWrapper=new QueryWrapper();
        return stuMapper.selectCount(queryWrapper);
    }
}
