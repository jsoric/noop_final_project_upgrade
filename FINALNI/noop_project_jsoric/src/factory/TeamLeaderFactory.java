package factory;

import entities.TeamLeader;
import entities.User;

/**
 * Factory for creating {@link TeamLeader} instances.
 */
public class TeamLeaderFactory extends UserFactory {

    /**
     * Creates a new {@link TeamLeader}.
     *
     * @return new team leader instance as {@link User}
     */
    @Override
    public User createUser() {
        return new TeamLeader();
    }
}