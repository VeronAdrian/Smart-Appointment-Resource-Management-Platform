package com.smartplatform.user.views;

import com.smartplatform.user.model.Tenant;
import com.smartplatform.user.service.TenantService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("tenant-management")
public class TenantManagementView extends VerticalLayout implements BeforeEnterObserver {
    public TenantManagementView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || auth.getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
            return;
        }
        add(new H2("Tenant Management - Super Admin"));
        TextField nameField = new TextField("Organization Name");
        TextField domainField = new TextField("Domain (unique identifier)");
        TextField emailField = new TextField("Contact Email");
        Button createButton =
                new Button(
                        "Create Tenant",
                        e -> {
                            if (nameField.isEmpty()
                                    || domainField.isEmpty()
                                    || emailField.isEmpty()) {
                                Notification.show("All fields are required");
                                return;
                            }
                            Tenant tenant =
                                    TenantService.createTenant(
                                            nameField.getValue(),
                                            domainField.getValue(),
                                            emailField.getValue());
                            if (tenant != null) {
                                Notification.show("Tenant created: " + tenant.getName());
                                nameField.clear();
                                domainField.clear();
                                emailField.clear();
                                refreshTenantList();
                            } else {
                                Notification.show("Error: Domain already exists");
                            }
                        });
        add(nameField, domainField, emailField, createButton);
        add(new H3("All Tenants:"));
        refreshTenantList();
    }

    private void refreshTenantList() {
        List<Tenant> tenants = TenantService.getAllTenants();
        if (tenants.isEmpty()) {
            add(new com.vaadin.flow.component.html.Span("No tenants defined yet."));
        } else {
            tenants.forEach(
                    tenant -> {
                        HorizontalLayout layout = new HorizontalLayout();
                        layout.add(
                                new com.vaadin.flow.component.html.Span(
                                        tenant.getName()
                                                + " ("
                                                + tenant.getDomain()
                                                + ") - Status: "
                                                + tenant.getStatus()));
                        ComboBox<String> statusCombo = new ComboBox<>("Status");
                        statusCombo.setItems("ACTIVE", "INACTIVE", "SUSPENDED");
                        statusCombo.setValue(tenant.getStatus());
                        statusCombo.addValueChangeListener(
                                e -> {
                                    if (TenantService.updateTenantStatus(
                                            tenant.getId(), e.getValue())) {
                                        Notification.show("Tenant status updated");
                                        refreshTenantList();
                                    }
                                });
                        Button deleteButton =
                                new Button(
                                        "Delete",
                                        deleteEvent -> {
                                            if (TenantService.deleteTenant(tenant.getId())) {
                                                Notification.show("Tenant deleted");
                                                removeAll();
                                                add(new H2("Tenant Management - Super Admin"));
                                                refreshTenantList();
                                            }
                                        });
                        layout.add(statusCombo, deleteButton);
                        add(layout);
                    });
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || auth.getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
            event.forwardTo("");
        }
    }
}
