package org.apache.guacamole.requestaccess.db;

import java.util.List;
import org.apache.guacamole.requestaccess.model.Request;
import org.apache.ibatis.annotations.Param;

public interface RequestMapper {

    List<Request> selectAll();

    void updateStatus(@Param("requestId") int requestId, @Param("status") String status);

}
