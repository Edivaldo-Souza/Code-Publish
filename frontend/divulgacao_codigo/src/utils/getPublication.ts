import { Publication } from "@/types/publication";
import api from "@/lib/api";
import { mapApiResponseToPublication } from "@/app/utils/publicationMapper";

export default async function getPublicationById(id: string): Promise<Publication> {
  try {
    const response = await api.get(`v1/publications/${id}`);

    const apiData = response.data.data;

    console.log("apiData",apiData);

    const publicationForForm = mapApiResponseToPublication(apiData);

    return publicationForForm;

  } catch (error) {
    console.error("Falha ao buscar publicação:", error);
    throw new Error("Não foi possível carregar a publicação.");
  }
}