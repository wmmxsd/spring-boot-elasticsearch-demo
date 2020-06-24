package com.wmm.elasticsearch.controller;

import com.wmm.elasticsearch.model.Device;
import com.wmm.elasticsearch.response.ResponseBean;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.security.RefreshPolicy;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangmingming160328
 * @Description 高级别Rest控制层
 * @date @2020/6/22 15:35
 */
@RestController
public class HighLevelRestController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RestHighLevelClient restClient;

    @Autowired
    public void setRestClient(@Qualifier("restHighLevelClient") RestHighLevelClient restClient) {
        this.restClient = restClient;
    }

    /**
     * 添加设备
     *
     * @param device 设备实体类
     * @return ResponseEntity
     * @throws IOException
     */
    @PostMapping("/device")
    public ResponseEntity<ResponseBean> addDevice(@RequestBody Device device) throws IOException {
        IndexRequest indexRequest = new IndexRequest("wmm", "device");
        long id = (long) (Math.random() * 100000);
        device.setId(id);
        indexRequest.id(String.valueOf(id)).source();
        IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
        return ResponseEntity.ok().body(new ResponseBean(HttpStatus.OK.value(), "add successful", indexResponse));
    }

    /**
     * 批量添加设备
     *
     * @param device 设备实体类
     * @return ResponseEntity
     * @throws IOException
     */
    @PostMapping("/devices")
    public ResponseEntity<ResponseBean> bulkAddDevices(@RequestBody Device device) throws IOException {
        BulkRequest bulkRequest = new BulkRequest("wmm", "device");
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.NONE);
        List<DocWriteRequest<?>> deviceList = new ArrayList<>();
        long id = (long) (Math.random() * 1000000);
        for (int index = 0; index < 5000; index++) {
            device.setId(id++);
            Map<String, Object> map = new HashMap<>(7);
            map.put("id", device.getId());
            map.put("name", device.getName());
            map.put("ip", device.getIp());
            map.put("mac", device.getMac());
            map.put("userName", device.getUserName());
            map.put("unit", device.getUnit());
            map.put("department", device.getDepartment());
            IndexRequest indexRequest = new IndexRequest("wmm", "device", String.valueOf(id));

            indexRequest.source(map);
            bulkRequest.add(indexRequest, 5000);
//            deviceList.add(indexRequest);
        }
//        bulkRequest.add(deviceList);
        restClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                log.info("hasFailures : {}, took: {}, message : {}", bulkItemResponses.hasFailures(), bulkItemResponses.getTook().toString(), bulkItemResponses.buildFailureMessage());
            }

            @Override
            public void onFailure(Exception e) {
                log.info(e.getCause().getMessage());
            }
        });
        return ResponseEntity.ok().body(new ResponseBean(200, "query successful", null));
    }

    /**
     * 更新设备
     * @param device 要更新的设备
     * @return ResponseEntity
     * @throws IOException
     */
    @PutMapping("device")
    public ResponseEntity<ResponseBean> edit(@RequestBody @NonNull Device device) throws IOException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        UpdateRequest request = new UpdateRequest("wmm", "device", String.valueOf(device.getId()));
        Field[] fieldArray = device.getClass().getDeclaredFields();
        List<Object> sourceList = new ArrayList<>();
        for (Field field : fieldArray){
            sourceList.add(field.getName());
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), Device.class);
            sourceList.add(pd.getReadMethod().invoke(device));
        }
        request.doc(sourceList.toArray());
        UpdateResponse response = restClient.update(request, RequestOptions.DEFAULT);
        return ResponseEntity.ok(new ResponseBean(200, "update successful", response));
    }

    @DeleteMapping("devices/{id}")
    public ResponseEntity<ResponseBean> deleteById(@PathVariable @NonNull String id) throws IOException {
        DeleteRequest request = new DeleteRequest();
        request.index("wmm").type("device").id(id);
        DeleteResponse response = restClient.delete(request, RequestOptions.DEFAULT);
        return ResponseEntity.ok(new ResponseBean(200, "delete successful", response));
    }

    @RequestMapping("/es")
    public ResponseEntity<ResponseBean> getEsInfo() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // SearchRequest
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        log.info(searchRequest.toString());
        // 查询ES
        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        return ResponseEntity.ok().body(new ResponseBean(200, "query successful", searchResponse));
    }

    /**
     * 列表查询
     *
     * @param page    当前页
     * @param rows    每页显示数量量
     * @param keyword 关键词
     * @return ResponseBean
     */
    @GetMapping("/devices")
    public ResponseBean list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows, String keyword) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 分页采用简单的from + size分页，适用数据量小的，了解更多分页方式可自行查阅资料
        searchSourceBuilder.from((page - 1) * rows);
        searchSourceBuilder.size(rows);
        // 查询条件，只有查询关键字不为空才带查询条件
        if (!StringUtils.isEmpty(keyword)) {
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "name", "userName");
            searchSourceBuilder.query(queryBuilder);
        }
        // 排序，根据ID倒叙
        searchSourceBuilder.sort("id", SortOrder.DESC);
        // SearchRequest
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("wmm").types("device").source(searchSourceBuilder);
        // 查询ES
        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        // 获取总数
        Long total = hits.getTotalHits();
        // 遍历封装列表对象
        List<Device> deviceList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            deviceList.add(JSON.parseObject(searchHit.getSourceAsString(), Device.class));
        }
        // 封装Map参数返回
        Map<String, Object> result = new HashMap<>(2);
        result.put("count", total);
        result.put("data", deviceList);
        return new ResponseBean(HttpStatus.OK.value(), "query successful", result);
    }

    @RequestMapping("/exception")
    public ResponseEntity<ResponseBean> error() {
        throw new NullPointerException("exception test");
    }
}
