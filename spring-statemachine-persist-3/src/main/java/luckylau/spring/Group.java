package luckylau.spring;

import lombok.Builder;
import lombok.Data;

/**
 * @Author luckylau
 * @Date 2022/3/26
 */
@Data
@Builder
public class Group {
    private int groupId;
    private int status;
    private boolean isAdvance;
}
