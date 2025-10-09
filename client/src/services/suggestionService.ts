import axios from "axios";

class suggestionService {
    async findAllCities(q: string) {
        return await axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/suggestions/cities?q=${q}`)
    }
    async findAllTransportUnits(q: string) {
        return await axios.get(`${process.env.VUE_APP_BACKEND_APP_API}/suggestions/transport?q=${q}`)
    }
}

export default new suggestionService();