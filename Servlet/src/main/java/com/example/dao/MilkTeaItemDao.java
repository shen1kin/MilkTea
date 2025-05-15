package com.example.dao;

import com.example.model.MilkTea;
import com.example.model.MilkTeaAttribute;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.*;

public class MilkTeaItemDao {

    //添加奶茶基础信息
    public int addMilkTea(String name, double price, byte[] image, String description, int classID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO MilkTeaList (name, price, image, description, is_deleted, class_id) VALUES (?, ?, ?, ?, 0, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setBytes(3, image);
            stmt.setString(4, description);
            stmt.setInt(5, classID); // 索引改为 5（注意不是6）
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<MilkTea> getMilkTeaList() {
        Map<Integer, MilkTea> milkTeaMap = new LinkedHashMap<>();

        String sql = "SELECT " +
                "    mt.id, " +
                "    mt.name AS milk_tea_name, " +
                "    mt.price, " +
                "    mt.image, " +
                "    mt.description, " +
                "    mc.name AS class_name, " +
                "    ma.name AS attribute_name, " +
                "    mav.value AS attribute_value " +
                "FROM MilkTeaList mt " +
                "LEFT JOIN MilkTeaClass mc ON mt.class_id = mc.id " +
                "LEFT JOIN MilkTeaAttributeMap mtam ON mt.id = mtam.milk_tea_id " +
                "LEFT JOIN MilkTeaAttributeValue mav ON mtam.attribute_value_id = mav.id " +
                "LEFT JOIN MilkTeaAttribute ma ON mav.attribute_id = ma.id " +
                "WHERE mt.is_deleted = 0 " +
                "  AND (mc.is_deleted = 0 OR mc.is_deleted IS NULL) " +
                "  AND (ma.is_deleted = 0 OR ma.is_deleted IS NULL) " +
                "  AND (mav.is_deleted = 0 OR mav.is_deleted IS NULL) " +
                "ORDER BY mt.id";

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");

                MilkTea milkTea = milkTeaMap.getOrDefault(id, null);
                if (milkTea == null) {
                    milkTea = new MilkTea();
                    milkTea.setId(id);
                    milkTea.setName(rs.getString("milk_tea_name"));
                    milkTea.setPrice(rs.getDouble("price"));
                    milkTea.setImage(rs.getBytes("image")); // base64
                    milkTea.setDescription(rs.getString("description"));
                    milkTea.setClazz(rs.getString("class_name"));
                    milkTea.setAttributes(new ArrayList<>());
                    milkTeaMap.put(id, milkTea);
                }

                String attrName = rs.getString("attribute_name");
                String attrValue = rs.getString("attribute_value");

                if (attrName != null && attrValue != null) {
                    // 查找是否已有该属性
                    Optional<MilkTeaAttribute> existingAttr = milkTea.getAttributes().stream()
                            .filter(a -> a.getAttribute().equals(attrName))
                            .findFirst();

                    if (existingAttr.isPresent()) {
                        existingAttr.get().getAttribute_value().add(attrValue);
                    } else {
                        MilkTeaAttribute attr = new MilkTeaAttribute();
                        attr.setAttribute(attrName);
                        attr.setAttribute_value(new ArrayList<>(List.of(attrValue)));
                        milkTea.getAttributes().add(attr);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(milkTeaMap.values());
    }



}
