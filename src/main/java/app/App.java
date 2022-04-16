package app;

import app.eventListener.eventListenerForMakeSomeRole;
import app.config.AppConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws LoginException, InterruptedException {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        JDA jda = JDABuilder.createDefault("OTYzNzAwNzEzMTM1OTM1NTE5.YlZ6Og.NRZrPInS-PBKz6i6CkhVbWh18Eo")
                .addEventListeners(new eventListenerForMakeSomeRole())
                .setActivity(Activity.playing("Ковыряется в очке")).build();

        jda.awaitReady().getCategories().get(0).getTextChannels().get(1).sendMessage("Huayse")
                .timeout(5, TimeUnit.SECONDS)
                .submit();

        context.close();

    }
}
