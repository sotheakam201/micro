package com.example.keycloak.initData;

import com.example.keycloak.domain.*;
import com.example.keycloak.feature.menu.repository.MenuRepository;
import com.example.keycloak.feature.menu.repository.PermissionRepository;
import com.example.keycloak.feature.menu.repository.RoleRepository;
import com.example.keycloak.feature.menu.repository.SubMenuRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final SubMenuRepository subMenuRepository;
    private final PermissionRepository permissionRepository;

    private List<String> statusMethods = List.of(
            "POST",
            "GET",
            "PUT",
            "DELETE"
    );

    @PostConstruct
    void init(){

        Role role = new Role();
        role.setName("admin");

        Role role1 = new Role();
        role1.setName("hr");

        if( roleRepository.count() == 0 ) {
            roleRepository.saveAll(List.of(role,role1));
        }


        initMenu(List.of(role,role1));
    }



    private void initMenu(List<Role> roles){
        Menu menu = new Menu();
        menu.setMenuNameEn("Product Management");

        if( menuRepository.count() == 0 ) {
            menuRepository.save(menu);

            SubMenu product = new SubMenu();
            product.setSubMenuNameEn("Product");
            product.setMenu(menu);

            SubMenu category = new SubMenu();
            category.setSubMenuNameEn("Category");
            category.setMenu(menu);

            subMenuRepository.saveAll(List.of(product,category));

            for( Role role : roles ) {

                if( role.getName().toUpperCase().trim().equals("ADMIN") ) {
                    for( String statusMethod : statusMethods  ) {
                        Permission permissionProduct = new Permission();
                        permissionProduct.setSubMenu(product);
                        permissionProduct.setPattern("/api/products/**");
                        permissionProduct.setStatusMethod(statusMethod);
                        permissionProduct.setRoles(List.of(role));
                        permissionRepository.save(permissionProduct);
                    }
                }

                if( role.getName().toUpperCase().trim().equals("HR") ) {
                    for( String statusMethod : statusMethods  ) {
                        if( statusMethod.equals("GET") ) {
                            Permission permissionProduct = new Permission();
                            permissionProduct.setSubMenu(product);
                            permissionProduct.setPattern("/api/products/**");
                            permissionProduct.setStatusMethod(statusMethod);
                            permissionProduct.setRoles(List.of(role));
                            permissionRepository.save(permissionProduct);
                        }
                    }
                }
            }

        }
    }
}
