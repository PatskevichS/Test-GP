package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EditRoomWindow extends AbstractEditAddWindow<RoomDto> {

    private final TextField roomNameField = new TextField(LanguageMessage.getText("name"));
    private final TextField roomDescriptionField = new TextField(LanguageMessage.getText("new.description"));

    public EditRoomWindow(final RoomDto roomDto, final RoomService roomService){
        super(LanguageMessage.getText("edit.room"));
        binder = new Binder<>();
        this.value = roomDto;
        roomNameField.setEnabled(false);
        form.addComponents(roomNameField, roomDescriptionField);
        settingBinder();
        addEventListener(roomService);
    }

    @Override
    protected void settingBinder() {
        binder.forField(roomNameField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                1, 10))
                .bind(RoomDto::getName, RoomDto::setName);
        binder.forField(roomDescriptionField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 20))
                .bind(RoomDto::getDescription, RoomDto::setDescription);
        binder.readBean(value);
    }

    protected void addEventListener(final RoomService roomService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.isValid()) {
                    binder.writeBean(value);
                    Notification.show(roomService.updateRoom(value));
                    this.close();
                } else {
                    Notification.show(LanguageMessage.getText("error"),
                            LanguageMessage.getText("valid.error"),
                            Notification.Type.WARNING_MESSAGE);
                }
            } catch (ValidationException e) {
                Notification.show(LanguageMessage.getText("wrong.value"));
            }
        });
        cancelButton.addClickListener(clickEvent -> {
            binder.readBean(value);
            Notification.show(LanguageMessage.getText("cancel"));
            this.close();
        });
    }
}
