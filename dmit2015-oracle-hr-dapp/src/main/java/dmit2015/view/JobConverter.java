package dmit2015.view;

import dmit2015.entity.Job;
import dmit2015.repository.HumanResourcesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
@FacesConverter(value = "jobConverter", managed = true)
public class JobConverter implements Converter<Job> {

    @Inject
    private HumanResourcesRepository hrRepository;

    @Override
    public Job getAsObject(FacesContext context, UIComponent component, String value) {
        return hrRepository.jobByJobId(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Job value) {
        return value.getJobId() == null ? "" : value.getJobId();
    }
}
