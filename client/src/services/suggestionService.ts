import axios from "axios";

class suggestionService {
    async findAllCities(q: string) {
        return await axios.get(`/suggestions/cities?q=${q}`)
    }
}

export default new suggestionService();