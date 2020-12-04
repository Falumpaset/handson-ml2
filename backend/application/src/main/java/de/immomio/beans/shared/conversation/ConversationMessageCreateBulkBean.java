package de.immomio.beans.shared.conversation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Freddy Sawma
 */

@Getter
@Setter
@NoArgsConstructor
public class ConversationMessageCreateBulkBean extends ConversationMessageCreateBean {

    private static final long serialVersionUID = -5943154823153809823L;

    private List<Long> recipientApplicationIds;
}
