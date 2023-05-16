package ru.task.converter;

import ru.task.enums.Model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ModelConverter implements AttributeConverter<Model, String> {
    @Override
    public String convertToDatabaseColumn(Model model) {
        return model.getName();
    }

    @Override
    public Model convertToEntityAttribute(String s) {
        for (Model model : Model.values()) {
            if (model.getName().equals(s)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid value for Model enum: " + s);
    }
}
