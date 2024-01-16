package com.kolya.gym.converter;

import com.kolya.gym.domain.TrainingType;
import javax.persistence.AttributeConverter;

public class TrainingTypeConverter implements AttributeConverter<TrainingType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TrainingType type) {
        switch (type) {
            case TYPE_1:
                return 1;
            case TYPE_2:
                return 2;
            case TYPE_3:
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public TrainingType convertToEntityAttribute(Integer val) {
        switch (val) {
            case 1:
                return TrainingType.TYPE_1;
            case 2:
                return TrainingType.TYPE_2;
            case 3:
                return TrainingType.TYPE_3;
            default:
                throw new IllegalStateException("Unexpected value: " + val);

        }
    }
}