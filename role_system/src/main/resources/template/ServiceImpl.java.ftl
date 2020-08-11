package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* <p>
* ${table.comment!} 服务实现类
* </p>
*
* @author RenShiWei
* @since 2020-08-06
*/
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    private final ${table.mapperName} ${table.entityPath}Mapper;

    <#--采用构造器注入-->
    public ${table.serviceImplName}(${table.mapperName} ${table.entityPath}Mapper){
        this.${table.entityPath}Mapper= ${table.entityPath}Mapper;
    }

}

