package org.apache.guacamole.requestaccess.db;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReasonMapper {

    List<String> select(@Param("contains") String contains);

}
