const SocketIO = require("socket.io");
// const socket = io();

const myFace = document.querySelector("#myFace");
const muteBtn = document.querySelector("#mute");
const cameraBtn = document.querySelector("#camera");
const camerasSelect = document.querySelector("#cameras");

const welcomeForm = document.querySelector("form");
const welcome = document.getElementById("welcome");
const call = document.getElementById("call");

call.hidden = true;

let myStream;
let roomName;
let muted = false;
let cameraOff = false;
let myPeerConnection;

async function getCameras() {
  try {
    const devices = await navigator.mediaDevices.enumerateDevices();
    const cameras = devices.filter((device) => device.kind === "videoinput");
    const currentCamera = myStream.getVideoTracks()[0];
    cameras.forEach((camera) => {
      const option = document.createElement("option");
      option.value = camera.deviceId;
      option.innerText = camera.label;
      camerasSelect.appendChild(option);
      if (currentCamera.label == camera.label) {
        option.selected = true;
      }
    });
    console.log(cameras);
  } catch (error) {
    console.log(error);
  }
}

async function getMedia(deviceId) {
  // device가 없을 때 실행
  const initialConstraints = {
    audio: true,
    video: { facingMode: "user" },
  };
  // device가 있을 때 실행
  const cameraConstraints = {
    audio: true,
    video: { deviceId: { exact: deviceId } },
  };
  try {
    myStream = await navigator.mediaDevices.getUserMedia({
      audio: true,
      video: true,
    });
    myFace.srcObject = myStream;
    if (!deviceId) {
      await getCameras();
    }
  } catch (error) {
    console.log(error);
  }
}

function handleMuteClick() {
  myStream.getAudioTracks().forEach((track) => (track.enabled = !track.enabled));
  muted = !muted;
  if (muted) {
    muteBtn.innerText = "UnMuted";
  } else {
    muteBtn.innerText = "Muted";
  }

  // console.log(myStream.getAudioTracks());
  // console.log(muted);
}
function handleCameraClick() {
  myStream.getVideoTracks().forEach((track) => (track.enabled = !track.enabled));

  if (cameraOff) {
    cameraBtn.innerText = "Camera OFF";
  } else {
    cameraBtn.innerText = "Camera ON";
  }
  cameraOff = !cameraOff;
  // console.log(cameraOff);
}
async function handleCameraChange() {
  await getMedia(camerasSelect.value);
  if (myPeerConnection) {
    const videoTrack = myStream.getVideoTracks()[0];
    const videoSender = myPeerConnection.getSenders().find((sender) => sender.track.kind === "video");
    videoSender.replaceTrack(videoTrack);
  }
}
async function initCall() {
  welcome.hidden = true;
  call.hidden = false;
  await getMedia();
  makeConnection();
}
async function handleWelcomeSubmit(e) {
  e.preventDefault();
  const input = welcomeForm.querySelector("input");
  await initCall();
  //input.value을 aa로 변경
  socket.emit("join_room", "aa");
  roomName = input.value;
  input.value = "";
}

muteBtn.addEventListener("click", handleMuteClick);
cameraBtn.addEventListener("click", handleCameraClick);
camerasSelect.addEventListener("input", handleCameraChange);
welcomeForm.addEventListener("submit", handleWelcomeSubmit);

// Socket Code
const socket = SocketIO(server, {
  path: "/socket",
  cors: {
    // 개발시
    // origin: "http://localhost:3000",
    // 배포시
    origin: "https://blurblur.kr",
  },
  transports: ["websocket", "polling"],
  allowEIO3: true,
});



socket.on("welcome", async () => {
  const offer = await myPeerConnection.createOffer();
  myPeerConnection.setLocalDescription(offer);
  console.log("send the offer");
  socket.emit("offer", offer, roomName);
});

socket.on("offer", async (offer) => {
  console.log("receive the offer");
  myPeerConnection.setRemoteDescription(offer);
  const answer = await myPeerConnection.createAnswer();
  myPeerConnection.setLocalDescription(answer);
  socket.emit("answer", answer, roomName);
  console.log("sent the answer ");
});

socket.on("answer", (answer) => {
  console.log("received the answer");
  myPeerConnection.setRemoteDescription(answer);
});

socket.on("ice", (ice) => {
  console.log("received the candidate");
  myPeerConnection.addIceCandidate(ice);
});
// RTC Code
function makeConnection() {
  myPeerConnection = new RTCPeerConnection({
    iceServers: [
      {
        urls: ["stun:stun.l.google.com:19302", "stun:stun1.l.google.com:19302", "stun:stun2.l.google.com:19302", "stun:stun3.l.google.com:19302", "stun:stun4.l.google.com:19302"],
      },
    ],
  });
  myPeerConnection.addEventListener("icecandidate", handleIce);
  myPeerConnection.addEventListener("track", handleTrack);
  myStream.getTracks().forEach((track) => myPeerConnection.addTrack(track, myStream));
}

function handleIce(data) {
  console.log("sent candidate");
  socket.emit("ice", data.candidate, roomName);
}

function handleAddStream(data) {
  // console.log("got an stream from my peer");
  // console.log(`Peer's Stream:`, data.stream);
  // console.log(`MY Stream:`, myStream);
  const peerFace = document.querySelector("#peerFace");
  peerFace.srcObject = data.stream;
}
function handleTrack(data) {
  console.log("handle track");
  const peerFace = document.querySelector("#peerFace");
  peerFace.srcObject = data.streams[0];
}
