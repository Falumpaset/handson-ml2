package de.immomio.beans.shared.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationConfigBean implements Serializable {
    private static final long serialVersionUID = 8370344192653527989L;

    private boolean allowed = false;
    private boolean replyOnly = false;
    private boolean whenInvited = false;
    private boolean forbidden = false;

    @JsonIgnore
    public boolean nothingIsSet() {
        return !allowed && !replyOnly && !whenInvited && !forbidden;
    }
}
