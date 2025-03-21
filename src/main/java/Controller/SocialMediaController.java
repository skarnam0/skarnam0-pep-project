package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount != null) {
            // returns registered account info
            ctx.json(mapper.writeValueAsString(registeredAccount));
        } else {
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);

        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            ctx.status(400);
            return;
        }

        if (accountService.getAccountById(message.getPosted_by()) == null) {
            ctx.status(400);
            return;
        }

        Message createdMessage = messageService.creatMessage(message);
        ctx.status(200).json(createdMessage);
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message == null) {
            ctx.status(200);
            ctx.result("");
        } else {
            ctx.json(message);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage == null) {
            ctx.status(200);
        } else {
            ctx.json(deletedMessage);
        }
    }

    private void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode request = mapper.readTree(ctx.body());

            String newMessage = request.get("message_text").asText();

            if (newMessage == null || newMessage.trim().isEmpty()) {
                ctx.status(400);
                return;
            }

            boolean success = messageService.updateMessage(messageId, newMessage);

            if (success) {
                Message updatedMessage = messageService.getMessageById(messageId);
                ctx.json(updatedMessage);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            ctx.status(400);
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        ctx.json(messages);
    }
}