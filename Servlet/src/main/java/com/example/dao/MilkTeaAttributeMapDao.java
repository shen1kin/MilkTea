package com.example.dao;

import com.example.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MilkTeaAttributeMapDao {

   //属性 与 属性值 链接
   public boolean linkMilkTeaWithAttributeValues(int milkTeaId, List<Integer> attributeValueIds) {
       String insertSql = "INSERT INTO MilkTeaAttributeMap (milk_tea_id, attribute_value_id) VALUES (?, ?)";
       try (Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertSql)) {

           for (int valueId : attributeValueIds) {
               stmt.setInt(1, milkTeaId);
               stmt.setInt(2, valueId);
               stmt.addBatch();
           }

           int[] results = stmt.executeBatch();
           return results.length == attributeValueIds.size(); // 全部插入成功

       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

}
