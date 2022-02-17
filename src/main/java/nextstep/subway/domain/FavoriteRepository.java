package nextstep.subway.domain;

import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMember(Member member);

    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long memberId);
}