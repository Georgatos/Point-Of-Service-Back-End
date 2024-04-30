package dev.andreasgeorgatos.tsilikos.service.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.PointsToEuroRatio;
import dev.andreasgeorgatos.tsilikos.repository.rewards.PointsToEuroRatioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointsToEuroRatioService {

    private final PointsToEuroRatioRepository pointsToEuroRatioRepository;

    @Autowired
    public PointsToEuroRatioService(PointsToEuroRatioRepository pointsToEuroRatioRepository) {
        this.pointsToEuroRatioRepository = pointsToEuroRatioRepository;
    }

    public ResponseEntity<List<PointsToEuroRatio>> getAllPointsToEuroRatio() {
        List<PointsToEuroRatio> pointsToEuroRatio = pointsToEuroRatioRepository.findAll();

        if (pointsToEuroRatio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pointsToEuroRatio);
    }

    public ResponseEntity<PointsToEuroRatio> getPointsToEuroRatiById(long id) {
        Optional<PointsToEuroRatio> optionalPointsToEuroRatio = pointsToEuroRatioRepository.findById(id);

        return optionalPointsToEuroRatio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<PointsToEuroRatio> createPointsToEuroRatio(PointsToEuroRatio membershipCard) {
        return ResponseEntity.ok(pointsToEuroRatioRepository.save(membershipCard));
    }

    @Transactional
    public ResponseEntity<PointsToEuroRatio> editPointsToEuroRatio(long id, PointsToEuroRatio pointsToEuroRatio) {
        Optional<PointsToEuroRatio> optionalPointsToEuroRatio = pointsToEuroRatioRepository.findById(id);

        if (optionalPointsToEuroRatio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PointsToEuroRatio foundPointsToEuroRatio = optionalPointsToEuroRatio.get();

        foundPointsToEuroRatio.setRealLifeMoney(pointsToEuroRatio.getRealLifeMoney());
        foundPointsToEuroRatio.setPointsWorth(pointsToEuroRatio.getPointsWorth());


        pointsToEuroRatioRepository.save(foundPointsToEuroRatio);
        return ResponseEntity.ok(foundPointsToEuroRatio);

    }

    @Transactional
    public ResponseEntity<PointsToEuroRatio> deletePointsToEuroRatioById(long id) {
        Optional<PointsToEuroRatio> optionalPointsToEuroRatio = pointsToEuroRatioRepository.findById(id);

        if (optionalPointsToEuroRatio.isPresent()) {
            pointsToEuroRatioRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
