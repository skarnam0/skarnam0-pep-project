package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message creatMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public boolean updateMessage(int messageId, String newMessage) {
        return messageDAO.updateMessage(messageId, newMessage);
    }

    public List<Message> getMessagesByUserId(int accountId) {
        return messageDAO.getMessagesByUserId(accountId);
    }
}
