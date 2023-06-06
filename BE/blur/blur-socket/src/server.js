import express from "express"; // express를 사용한 일반적인 NodeJS
import http from "http";
import { Server } from "socket.io";
import cors from "cors";

const app = express();

app.use(cors());

// express를 이용해 http 서버를 만듦(노출 서버)
const httpServer = http.createServer(app);

// 로컬 / ec2서버
// cors: http://localhost:3000  /  [https://admin.socket.io]
// httpServer.listen: 3001  /  https://i8b307.p.ssafy.io

// // http 서버 위에 ws(webSocket) 서버를 만듦
const wsServer = new Server(httpServer, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
    credentials: true,
  },
});

const {
  sockets: {
    adapter: { sids, rooms },
  },
} = wsServer;

wsServer.on("connection", (socket) => {
  console.log("connecting 성공, 서버에 도달");

  socket.on("join_room", async (roomName) => {
    console.log("브라우저에서 받은 roomName : ", roomName);
    socket.join(roomName); // 방에 들어가는거
    socket.to(roomName).emit("welcome", rooms);
    socket.to(roomName).emit("roomsCheck", rooms);
    console.log(sids);
    console.log(rooms);
  });
  socket.on("offer", (offer, roomName) => {
    socket.to(roomName).emit("offer", offer);
    console.log("");
  });
  socket.on("answer", (answer, roomName) => {
    socket.to(roomName).emit("answer", answer);
  });
  socket.on("ice", (ice, roomName) => {
    socket.to(roomName).emit("ice", ice);
  });
  socket.on("leave-room", (roomName, done) => {
    socket.to(roomName).emit("peer-leaving");
    socket.leave(roomName);
    done();
  });
  socket.on("disconnecting", () => {
    socket.rooms.forEach((room) => {
      socket.to(room).emit("peer-leaving");
      socket.leave(room);
    });
  });
});
const handleListen = () => console.log(`Listening on https://i8b307.p.ssafy.io`);
httpServer.listen(3001, handleListen);
