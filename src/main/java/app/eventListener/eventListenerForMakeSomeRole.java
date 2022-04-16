package app.eventListener;


import app.discord.DiscordRoles;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.internal.entities.RoleImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;


@Repository
public class eventListenerForMakeSomeRole  extends ListenerAdapter {

    Logger logger = LoggerFactory.getLogger(eventListenerForMakeSomeRole.class);
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    private final SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);

        try(Session session = sessionFactory.openSession()) {
            String nickName = "'" + event.getUser().getName() + "'";

            session.beginTransaction();

            Query roleNameQuery = session.createNativeQuery("select discord_role_name from discord_roles join discord_roles_for_users \n" +
                    "                    on discord_roles_for_users.discord_id_roles = discord_roles.id_discord_role join users " +
                    "on discord_roles_for_users.id_user = users.id_user\n" +
                    "                    where username = " + nickName);

            String roleName = (String) roleNameQuery.getSingleResult();
            Query discordRoleIdForUpdateQuery = session.createNativeQuery("select discord_id_role from discord_roles  join discord_roles_for_users \n" +
                    "                    on discord_roles_for_users.discord_id_roles = discord_roles.id_discord_role join users " +
                    "on discord_roles_for_users.id_user = users.id_user\n" +
                    "                    where username = " + nickName);
            Long discordRoleIdForUpdate = ((BigInteger) discordRoleIdForUpdateQuery.getSingleResult()).longValue();

            String roleNameForDelete = "'" + DiscordRoles.delPreviousRole(roleName) + "'";
            Query discordRoleIdForDeleteQuery = session.createNativeQuery("select discord_id_role from discord_roles where discord_role_name = " + roleNameForDelete);
            Long discordRoleIdForDelete = ((BigInteger) discordRoleIdForDeleteQuery.getSingleResult()).longValue();

            System.out.println(roleName);
            System.out.println(event.getGuild().getMember(event.getUser()).getRoles());
            System.out.println("_____________________________________________________");
            logger.info(String.valueOf(roleName));
            if (session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
                session.getTransaction().commit();
            }
            for (Role r : event.getGuild().getMember(event.getUser()).getRoles()) {
                if (DiscordRoles.allRolesFromSite(r.getName())) {
                    event.getGuild().removeRoleFromMember(event.getMember().getUser().getId(), new RoleImpl(discordRoleIdForDelete, event.getGuild())).queue();
                    event.getGuild().addRoleToMember(event.getMember().getUser().getId(), new RoleImpl(discordRoleIdForUpdate, event.getGuild())).queue();
                }
            }
        }
    }
}


