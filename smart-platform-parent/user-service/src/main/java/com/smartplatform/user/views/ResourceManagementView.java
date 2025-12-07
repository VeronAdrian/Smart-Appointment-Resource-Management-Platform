package com.smartplatform.user.views;

import com.smartplatform.user.model.Resource;
import com.smartplatform.user.service.ResourceService;
import com.smartplatform.user.service.TenantContext;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("resource-management")
public class ResourceManagementView extends VerticalLayout implements BeforeEnterObserver {
    private final ResourceService resourceService;

    public ResourceManagementView(ResourceService resourceService, TenantContext tenantContext) {
        this.resourceService = resourceService;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))
                        && auth.getAuthorities().stream()
                                .noneMatch(a -> a.getAuthority().equals("STAFF")))) {
            return;
        }
        add(new H2("Resource Management"));
        TextField nameField = new TextField("Resource Name");
        TextField typeField = new TextField("Resource Type (e.g., Gym Equipment, Meeting Room)");
        TextArea descriptionField = new TextArea("Description");
        IntegerField maxHoursField = new IntegerField("Max Reservation Hours");
        maxHoursField.setValue(2);
        Button createButton =
                new Button(
                        "Create Resource",
                        e -> {
                            if (nameField.isEmpty() || typeField.isEmpty()) {
                                Notification.show("Name and Type are required");
                                return;
                            }
                            String tenantId = tenantContext.getCurrentTenantId();
                            Resource resource =
                                    resourceService.createResource(
                                            nameField.getValue(),
                                            typeField.getValue(),
                                            descriptionField.getValue(),
                                            maxHoursField.getValue() != null
                                                    ? maxHoursField.getValue()
                                                    : 2,
                                            tenantId);
                            if (resource != null) {
                                Notification.show("Resource created: " + resource.getName());
                                nameField.clear();
                                typeField.clear();
                                descriptionField.clear();
                                maxHoursField.setValue(2);
                                refreshResourceList();
                            }
                        });
        add(nameField, typeField, descriptionField, maxHoursField, createButton);
        add(new H3("Existing Resources:"));
        refreshResourceList();
    }

    private void refreshResourceList() {
        List<Resource> resources = resourceService.getAllResources();
        if (resources.isEmpty()) {
            add(new com.vaadin.flow.component.html.Span("No resources defined yet."));
        } else {
            resources.forEach(
                    resource -> {
                        HorizontalLayout layout = new HorizontalLayout();
                        layout.add(
                                new com.vaadin.flow.component.html.Span(
                                        resource.getName()
                                                + " ("
                                                + resource.getType()
                                                + ") - "
                                                + (resource.isAvailable()
                                                        ? "Available"
                                                        : "Unavailable")));
                        Button deleteButton =
                                new Button(
                                        "Delete",
                                        deleteEvent -> {
                                            if (resourceService.deleteResource(resource.getId())) {
                                                Notification.show("Resource deleted");
                                                removeAll();
                                                add(new H2("Resource Management"));
                                                add(
                                                        new TextField("Resource Name"),
                                                        new TextField(
                                                                "Resource Type (e.g., Gym"
                                                                    + " Equipment, Meeting Room)"),
                                                        new TextArea("Description"),
                                                        new IntegerField("Max Reservation Hours"),
                                                        new Button("Create Resource")); // Re-adding
                                                // components
                                                // simplified
                                                // for refresh
                                                // logic,
                                                // ideally extract init method
                                                refreshResourceList();
                                            }
                                        });
                        layout.add(deleteButton);
                        add(layout);
                    });
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))
                        && auth.getAuthorities().stream()
                                .noneMatch(a -> a.getAuthority().equals("STAFF")))) {
            event.forwardTo("");
        }
    }
}
