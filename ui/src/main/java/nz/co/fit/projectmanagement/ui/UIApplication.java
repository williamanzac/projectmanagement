package nz.co.fit.projectmanagement.ui;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class UIApplication extends Application<UIConfiguration> {

    public static void main(final String[] args) throws Exception {
        new UIApplication().run(args);
    }

    @Override
    public String getName() {
        return "UI";
    }

    @Override
    public void initialize(final Bootstrap<UIConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final UIConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
