package com.gaoxiaocha.controller;

import com.alibaba.fastjson.JSONObject;
import com.gaoxiaocha.dto.Result;
import com.gaoxiaocha.pojo.Goods;
import com.gaoxiaocha.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @RequestMapping(value = "/goods/insert", method = RequestMethod.POST)
    @ResponseBody
    public String insert(String gname,Double gprice,String gdescription,Integer gstock,String gpicture){
        Goods goods = new Goods(null,gname,gprice,gdescription,gstock,gpicture);
        Result result = new Result();
        if(goodsService.charu(goods)){
            result.setMsg("插入商品成功！");
            result.setSuccess(true);
        }
        else{
            result.setMsg("插入商品失败！");
            result.setSuccess(false);
        }
        return JSONObject.toJSONString(result);
        }
    @RequestMapping(value = "/goods/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Integer gid){
        Goods goods = new Goods();
        goods.setGid(gid);
        Result result = new Result();
        if(goodsService.shanchu(goods)){
            result.setMsg("删除商品成功！");
            result.setSuccess(true);
        }
        else{
            result.setMsg("删除商品失败！");
            result.setSuccess(false);
        }
        return JSONObject.toJSONString(result);
    }
    @RequestMapping(value = "/goods/updatePrice", method = RequestMethod.POST)
    @ResponseBody
    public String updatePrice(Integer gid,Double gprice){
        Goods goods = new Goods();
        goods.setGid(gid);
        goods.setGprice(gprice);
        Result result = new Result();
        if(goodsService.xiugaijiage(goods)){
            result.setMsg("更新商品价格成功！");
            result.setSuccess(true);
        }
        else{
            result.setMsg("更新商品价格失败！");
            result.setSuccess(false);
        }
        return JSONObject.toJSONString(result);
    }
    @RequestMapping(value = "/goods/updateStock", method = RequestMethod.POST)
    @ResponseBody
    public String updateStock(Integer gid,Integer gstock){
        Goods goods = new Goods();
        goods.setGid(gid);
        goods.setGstock(gstock);
        Result result = new Result();
        if(goodsService.xiugaikucun(goods)){
            result.setMsg("更新商品库存成功！");
            result.setSuccess(true);
        }
        else{
            result.setMsg("更新商品库存失败！");
            result.setSuccess(false);
        }
        return JSONObject.toJSONString(result);
    }
    @RequestMapping(value = "/goods/select", method = RequestMethod.POST)
    @ResponseBody
    public String select(Integer gid) {
        Goods goods = new Goods();
        goods.setGid(gid);
        Result<Goods> result = new Result();
        goods = goodsService.chazhao(goods);
            result.setMsg("查询成功！");
            result.setData(goods);
            result.setSuccess(true);

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/goods/selectAll", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String selectAll() {


        Result<List<Goods>> result = new Result();
        List<Goods> chazhaosuoyou = goodsService.chazhaosuoyou();
        if(chazhaosuoyou.size()>0||chazhaosuoyou!=null){
            result.setMsg("查询成功！");
            result.setData(chazhaosuoyou);
            result.setSuccess(true);
        }else{
            result.setMsg("查询失败！");
            result.setSuccess(false);
        }

        return JSONObject.toJSONString(result);
    }
}



