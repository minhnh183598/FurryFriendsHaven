package org.demo.huyminh.Event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Request.InvitationEventData;
import org.springframework.context.ApplicationEvent;

/**
 * @author Minh
 * Date: 10/22/2024
 * Time: 12:20 AM
 */


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskInvitationEvent extends ApplicationEvent {

    InvitationEventData invitationEventData;

    public TaskInvitationEvent(InvitationEventData invitationEventData) {
        super(invitationEventData);
        this.invitationEventData = invitationEventData;
    }
}
