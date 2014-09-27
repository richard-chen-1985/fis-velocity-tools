package com.baidu.fis.velocity.directive;

import com.baidu.fis.velocity.util.Resource;
import com.baidu.fis.velocity.event.IncludeFisSource;
import com.baidu.fis.velocity.util.ResourceManager;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.Parse;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by 2betop on 5/21/14.
 */
abstract public class AbstractInclude extends Parse {
    protected Log log;

    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
        log = rs.getLog();
        super.init(rs, context, node);
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        Resource fisResource = ResourceManager.ref(context);

        //  支持 include fis id.
        IncludeFisSource eh = new IncludeFisSource();
        eh.setFisResource(fisResource);


        rsvc.getApplicationEventCartridge().addEventHandler(eh);


        try {
            // 添加资源 Like Require
            fisResource.addResource(node.jjtGetChild(0).value(context).toString());
        } catch (Exception err) {
            // do nothings
        }

        Boolean result = super.render(context, writer, node);

        eh.setFisResource(null);
        rsvc.getApplicationEventCartridge().removeEventHandler(eh);
        ResourceManager.unRef(context);

        return result;
    }
}
