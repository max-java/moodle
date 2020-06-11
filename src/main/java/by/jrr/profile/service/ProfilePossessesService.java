package by.jrr.profile.service;

import by.jrr.feedback.bean.EntityType;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.bean.ProfilePossesses;
import by.jrr.profile.repository.ProfilePossessesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilePossessesService {

    @Autowired
    ProfilePossessesRepository profilePossessesRepository;

    @Autowired
    ProfileService profileService;

    public boolean isUserOwner(Long profleId, Long entityId) {
        Optional<ProfilePossesses> possess = profilePossessesRepository.findByProfileIdAndEntityId(profleId, entityId);
        return possess.isPresent();
    }

    public void savePossessForCurrentUser(Long entityId, EntityType type) {

            ProfilePossesses possess = ProfilePossesses.builder()
                    .profileId(profileService.getCurrentUserProfile().getId())
                    .entityId(entityId)
                    .entityType(type)
                    .build();
        try {
            profilePossessesRepository.save(possess);
        } catch (Exception ex) {
            // TODO: 12/06/20 log exception: possible id duplicates in unique fields.
        }
    }
}
