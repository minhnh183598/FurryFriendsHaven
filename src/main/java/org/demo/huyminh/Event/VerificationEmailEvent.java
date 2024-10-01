package org.demo.huyminh.Event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Minh
 * Date: 9/28/2024
 * Time: 7:11 PM
 */

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationEmailEvent extends ApplicationEvent {

    User user;

    public VerificationEmailEvent(User user) {
        super(user);
        this.user = user;
    }
}
