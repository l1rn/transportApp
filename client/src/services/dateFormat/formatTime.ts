export const formatToDatabase = (template: string) => {
    const date = new Date(template);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    const formatedToDatabase = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    return formatedToDatabase;
}


export const getDateSource = (route: any, isArrival = false) => {
    const timeString = isArrival ? route.arrivalTime : route.time;
    
    if(timeString.includes(' ')) {
    return timeString.split(' ')[0];
    }
    return route.date;
    };
    
export const formatDate = (dateString: string) => {
    try {
    const [month, day] = dateString.split('-');
    return `${day}-${month}`;
    } catch {
    return '??-??';
    }
    };
    
export const formatTime = (timeString: string) => {
    try {
    const timePart = timeString.includes(' ') 
      ? timeString.split(' ')[1] 
      : timeString;
    return timePart.slice(0, 5);
    } catch {
    return '--:--';
    }
};