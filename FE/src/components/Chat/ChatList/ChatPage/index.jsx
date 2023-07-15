import React, { useEffect, useState, useRef } from "react";
import "./index.css";
import ChatPageDialogueMe from "./ChatDialogueMe";
import ChatPageDialogueYou from "./ChatDialogueYou";


export function ChatPage({ showChatPage, chatRoom }) {
  const [messages, setMessages] = useState([]);
  const [messageInput, setMessageInput] = useState("");
  const chatContentRef = useRef(null);
  const socketUrl = `wss://blurblur.kr/api/ws?userId=${chatRoom.myId}&roomId=${chatRoom.id}`;
  // const socketUrl = `ws://localhost:8081/ws?userId=${chatRoom.myId}&roomId=${chatRoom.id}`;
  const initialCursor = useRef("-1.0");
  

  useEffect(() => {
    const socket = new WebSocket(socketUrl);

    const handleSocketMessage = (event) => {
      const message = JSON.parse(event.data);

      if (Array.isArray(message)) {
        setMessages((prevMessages) => [...prevMessages, ...message]);
      } else if (message.cursor !== undefined && message.cursor !== "-1.0") {
        const jsonStr = JSON.stringify({ cursor: message.cursor });
        socket.send(jsonStr);
        initialCursor.current = message.cursor;
      } else if (message.message !== undefined) {
        setMessages((prevMessages) => [message, ...prevMessages]);
      }
    };

    socket.addEventListener("open", () => {
      if (messageInput.trim() !== "") {
        socket.send(
          JSON.stringify({
            nickname: chatRoom.myName,
            message: messageInput,
          })
        );
      }
    });

    socket.addEventListener("message", handleSocketMessage);

    return () => {
      socket.removeEventListener("message", handleSocketMessage);
      socket.close();
    };
  }, [socketUrl]);

  useEffect(() => {
    if (chatContentRef.current) {
      chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight;
    }
  }, [messages]);

  const sendMessage = () => {
    if (messageInput.trim() !== "") {
      const newMessage = {
        nickname: chatRoom.myName,
        message: messageInput,
      };

      const socket = new WebSocket(socketUrl);

      socket.addEventListener("open", () => {
        const jsonStr = JSON.stringify(newMessage);

        socket.send(jsonStr);
      });

      socket.addEventListener("message", (event) => {
        console.log("Message sent:", event.data);
      });

      socket.addEventListener("close", () => {
        socket.close();
      });

      if (newMessage.nickname === chatRoom.myName) {
        setMessages((prevMessages) => [newMessage, ...prevMessages]);
      } else {
        setMessages((prevMessages) => [...prevMessages, newMessage]);
      }
      setMessageInput("");
    }
  };


  return (
    <div className="ChatPageBack">
      <div className="ChatPageHeader">
        <div className="ChatPageHeaderBtn" onClick={showChatPage}></div>
        <div className="ChatPageHeaderName">{chatRoom.opponentName}</div>
      </div>
  
      <div className="ChatPageContent" ref={chatContentRef}>
        <div className="ChatPageDialogue">
          {messages.slice(0).reverse().map((message, index) => (
            <div key={index}>
              {message.nickname === chatRoom.myName ? (
                <ChatPageDialogueMe key={index} content={message.message} />
              ) : (
                <ChatPageDialogueYou key={index} content={message.message} chatRoom={chatRoom} />
              )}
            </div>
          ))}
        </div>
      </div>
  
      <div className="ChatPageInputDiv">
        <div className="ChatPageInput">
          <input
            className="ChatPageInputMessage"
            value={messageInput}
            onChange={(e) => setMessageInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                e.preventDefault();
                sendMessage();
              }
            }}
          />
          <button className="ChatPageInputButton" onClick={sendMessage}/>
        </div>
      </div>
    </div>
  );
}

export default ChatPage;



