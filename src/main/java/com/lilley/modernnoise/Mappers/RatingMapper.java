package com.lilley.modernnoise.Mappers;

import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;

public class RatingMapper {
    private RatingMapper(){}

    public static RatingDto toDto(Rating rating){
        return new RatingDto(rating.getAlbum().getAudioDbId(), rating.getScore());
    }

}
