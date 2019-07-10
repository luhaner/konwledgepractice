package com.knowledge.practice.dao.mapper;

import com.knowledge.practice.model.RsaKeys;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * RAS秘钥Mapper类
 */
public interface KldRasKeysMapper {

    @Insert("INSERT INTO l_rsa_keys (id, device, source,RSA_pub, RSA_priv, create_time, last_update_time) "
            + "VALUES (#{id}, #{device}, #{source},#{rsaPub}, #{rsaPriv}, #{createTime}, #{lastUpdateTime})")
    /**
     * 
     * 插入RAS秘钥数据
     * 
     * @param record 秘钥数据
     * @return 插入结果
     */
    int insertSelective(RsaKeys record);

    @Select("SELECT id, device, source, RSA_pub as rsaPub, RSA_priv as rsaPriv FROM l_rsa_keys WHERE device = #{device} AND source = #{source}")
    /**
     * 根据设备号和来源查询秘钥
     * 
     * @param device 设备号
     * @param source 来源
     * @return 秘钥
     *
     */
    List<RsaKeys> findKldRsaKeysByDevice(@Param("device") String device, @Param("source") String source);
}
