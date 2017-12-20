package arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import shopit.CriterionBuilder;
import shopit.asynchronous.CommunicationMessageBean;
import shopit.components.ShopListRegistryBean;
import shopit.entities.User;
import shopit.exceptions.ContributorAlreadyExistingException;
import shopit.utils.NotificationType;

public abstract class AbstractTCFTest
{
	@Deployment
	public static WebArchive createDeployment()
    {
		return ShrinkWrap.create(WebArchive.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml")
				
                .addPackage(User.class.getPackage())
				.addPackage(ShopListRegistryBean.class.getPackage())
				.addPackage(ContributorAlreadyExistingException.class.getPackage())
				.addPackage(NotificationType.class.getPackage())
				.addPackage(CriterionBuilder.class.getPackage())
				.addPackage(CommunicationMessageBean.class.getPackage());
	}
}
