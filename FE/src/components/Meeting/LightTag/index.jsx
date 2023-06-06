export default function LightTag({ showLight }) {
  return (
    <>
      <div className="lightTagsDiv"></div>
      <div className="lightTagBtn" onClick={showLight}></div>
    </>
  );
}
