package claygminx.worshipppt.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiteeReleaseEntity {

    private Long id;
    private String tag_name;
    private String name;
    private String body;
    private Date created_at;
}
