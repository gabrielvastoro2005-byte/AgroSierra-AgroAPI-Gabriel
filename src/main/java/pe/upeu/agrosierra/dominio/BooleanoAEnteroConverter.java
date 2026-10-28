package pe.upeu.agrosierra.dominio;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Adapta booleanos del dominio al NUMBER(1) usado por Oracle en el esquema. */
@Converter
public class BooleanoAEnteroConverter implements AttributeConverter<Boolean, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Boolean value) {
        return Boolean.TRUE.equals(value) ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer value) {
        return value != null && value != 0;
    }
}
