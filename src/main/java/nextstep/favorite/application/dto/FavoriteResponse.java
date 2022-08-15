package nextstep.favorite.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

@Getter
@AllArgsConstructor
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
    }

}