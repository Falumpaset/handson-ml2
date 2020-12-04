package de.immomio.beans.shared.conversation;

import de.immomio.data.shared.bean.common.S3File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Freddy Sawma
 */

@Getter
@Setter
@NoArgsConstructor
public class ConversationMessageCreateBean implements Serializable {

    private static final long serialVersionUID = 842397924555751213L;

    private String message;
    private List<S3File> attachments;
}
