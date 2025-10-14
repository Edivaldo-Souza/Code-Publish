
interface VotingComponentProps{
    upvotes?:number,
    downvotes?:number
}

const UpArrowIcon = () => (
  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 15l7-7 7 7"></path></svg>
);
const DownArrowIcon = () => (
  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7"></path></svg>
);

export default function VotingComponent({upvotes,downvotes}:VotingComponentProps){
    return(
        <div className="flex items-center gap-3 flex-shrink-0">
      <button className="flex items-center justify-center w-11 h-11 rounded-full bg-green-100 text-green-600 hover:bg-green-200 transition-colors">
        <UpArrowIcon />
      </button>
      <span className="text-2xl font-bold text-green-600">{upvotes}</span>
      <button className="flex items-center justify-center w-11 h-11 rounded-full bg-red-100 text-red-600 hover:bg-red-200 transition-colors">
        <DownArrowIcon />
      </button>
      <span className="text-2xl font-bold text-red-600">{downvotes}</span>
    </div>
    )
}