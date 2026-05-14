package ec.insti.medista.common.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MedistaRevisionListener implements RevisionListener{

    @Override
    public void newRevision(Object revisionEntity) {

        MedistaRevisionEntity revision = (MedistaRevisionEntity) revisionEntity;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Long userId) {
            revision.setUserId(userId);
        }


    }

}
